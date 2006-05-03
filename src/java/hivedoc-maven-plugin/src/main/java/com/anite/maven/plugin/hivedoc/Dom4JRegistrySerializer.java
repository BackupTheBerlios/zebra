// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.anite.maven.plugin.hivedoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Attribute;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.ModuleDescriptorProvider;
import org.apache.hivemind.Occurances;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.internal.Visibility;
import org.apache.hivemind.parse.AttributeMappingDescriptor;
import org.apache.hivemind.parse.ConfigurationPointDescriptor;
import org.apache.hivemind.parse.ContributionDescriptor;
import org.apache.hivemind.parse.ConversionDescriptor;
import org.apache.hivemind.parse.CreateInstanceDescriptor;
import org.apache.hivemind.parse.DependencyDescriptor;
import org.apache.hivemind.parse.ImplementationDescriptor;
import org.apache.hivemind.parse.InstanceBuilder;
import org.apache.hivemind.parse.InterceptorDescriptor;
import org.apache.hivemind.parse.InvokeFactoryDescriptor;
import org.apache.hivemind.parse.ModuleDescriptor;
import org.apache.hivemind.parse.ServicePointDescriptor;
import org.apache.hivemind.parse.SubModuleDescriptor;
import org.apache.hivemind.schema.AttributeModel;
import org.apache.hivemind.schema.ElementModel;
import org.apache.hivemind.schema.Rule;
import org.apache.hivemind.schema.impl.SchemaImpl;
import org.apache.hivemind.schema.rules.CreateObjectRule;
import org.apache.hivemind.schema.rules.InvokeParentRule;
import org.apache.hivemind.schema.rules.PushAttributeRule;
import org.apache.hivemind.schema.rules.PushContentRule;
import org.apache.hivemind.schema.rules.ReadAttributeRule;
import org.apache.hivemind.schema.rules.ReadContentRule;
import org.apache.hivemind.schema.rules.SetModuleRule;
import org.apache.hivemind.schema.rules.SetParentRule;
import org.apache.hivemind.schema.rules.SetPropertyRule;
import org.apache.hivemind.util.IdUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * This is derived from the basic hivemind one but uses Dom4j
 * 
 * 
 * This class serializes a set of {@link ModuleDescriptor module descriptors}
 * into a {@link Document XML document}. The set of module descriptors to
 * process is specified indirectly by supplying one or several
 * {@link ModuleDescriptorProvider} (see
 * {@link #addModuleDescriptorProvider(ModuleDescriptorProvider)}). In this
 * respect this class is used the same way as
 * {@link org.apache.hivemind.impl.RegistryBuilder}. There is even a
 * corresponding {@link #createDefaultRegistryDocument() static method} to
 * serialize the modules of the default registry.
 * <p>
 * The resulting XML file does not conform to the hivemind module deployment
 * descriptor schema. The following changes occur:
 * <ul>
 * <li>The outermost element is &lt;registry&gt; (which contains a list of
 * &lt;module&gt;)
 * <li>A unique id (unique within the file) is assigned to each &lt;module&gt;,
 * &lt;configuration-point&gt;, &lt;service-point&gt;, &lt;contribution&gt;,
 * &tl;schema&gt; and &lt;implementation&gt; (this is to make it easier to
 * generate links and anchors)
 * <li>Unqualified ids are converted to qualified ids (whereever possible).
 * </ul>
 * 
 * @author Knut Wannheden
 * @since 1.1
 */
public class Dom4JRegistrySerializer {
	private Set _processedSchemas = new HashSet();

	private List _providers = new ArrayList();

	private ErrorHandler _handler;

	private Document _document;

	private ModuleDescriptor _md;

	public Dom4JRegistrySerializer() {
		_handler = new DefaultErrorHandler();
	}

	public void addModuleDescriptorProvider(ModuleDescriptorProvider provider) {
		_providers.add(provider);
	}

	public Document createRegistryDocument() {

		_document = DocumentHelper.createDocument();

		Element registry = DocumentHelper.createElement("registry");
		_document.add(registry);

		for (Iterator i = _providers.iterator(); i.hasNext();) {
			ModuleDescriptorProvider provider = (ModuleDescriptorProvider) i
					.next();

			processModuleDescriptorProvider(registry, provider);
		}

		return _document;
	}

	private void processModuleDescriptorProvider(Element registry,
			ModuleDescriptorProvider provider) {
		for (Iterator j = provider.getModuleDescriptors(_handler).iterator(); j
				.hasNext();) {
			_md = (ModuleDescriptor) j.next();

			Element module = getModuleElement(_md);
			registry.add(module);

		}
	}

	private Element getModuleElement(ModuleDescriptor md) {
		Element module = DocumentHelper.createElement("module");

		module.addAttribute("id", md.getModuleId());
		module.addAttribute("version", md.getVersion());
		module.addAttribute("package", md.getPackageName());

		if (md.getAnnotation() != null) {
			module.addText(md.getAnnotation());
		}

		addDependencies(module);

		addServicePoints(module);

		addConfigurationPoints(module);

		addContributions(module);

		addImplementations(module);

		addSchemas(module);

		addSubModules(module);

		return module;
	}

	private void addDependencies(Element module) {
		List dependencies = _md.getDependencies();

		if (dependencies != null) {
			for (Iterator i = dependencies.iterator(); i.hasNext();) {
				DependencyDescriptor dd = (DependencyDescriptor) i.next();

				Element dependency = getDependencyElement(dd);

				module.add(dependency);
			}
		}
	}

	private void addServicePoints(Element module) {
		List servicePoints = _md.getServicePoints();

		if (servicePoints != null) {
			for (Iterator i = servicePoints.iterator(); i.hasNext();) {
				ServicePointDescriptor spd = (ServicePointDescriptor) i.next();

				Element servicePoint = getServicePointElement(spd);

				module.add(servicePoint);

				SchemaImpl s = (SchemaImpl) spd.getParametersSchema();

				if (s != null && s.getId() != null)
					addSchema(module, s, "schema");
			}
		}
	}

	private void addConfigurationPoints(Element module) {
		List configurationPoints = _md.getConfigurationPoints();

		if (configurationPoints != null) {
			for (Iterator i = configurationPoints.iterator(); i.hasNext();) {
				ConfigurationPointDescriptor cpd = (ConfigurationPointDescriptor) i
						.next();

				Element configurationPoint = getConfigurationPointElement(cpd);

				module.add(configurationPoint);

				SchemaImpl s = (SchemaImpl) cpd.getContributionsSchema();

				if (s != null && s.getId() != null)
					addSchema(module, s, "schema");
			}
		}
	}

	private void addContributions(Element module) {
		List contributions = _md.getContributions();

		if (contributions != null) {
			for (Iterator i = contributions.iterator(); i.hasNext();) {
				ContributionDescriptor cd = (ContributionDescriptor) i.next();

				Element contribution = getContributionElement(cd);

				module.add(contribution);
			}
		}
	}

	private void addImplementations(Element module) {
		List implementations = _md.getImplementations();

		if (implementations != null) {
			for (Iterator i = implementations.iterator(); i.hasNext();) {
				ImplementationDescriptor id = (ImplementationDescriptor) i
						.next();

				Element implementation = getImplementationElement(id);

				module.add(implementation);
			}
		}
	}

	private void addSchemas(Element module) {
		Collection schemas = _md.getSchemas();

		for (Iterator i = schemas.iterator(); i.hasNext();) {
			SchemaImpl s = (SchemaImpl) i.next();

			addSchema(module, s, "schema");
		}
	}

	private void addSubModules(Element module) {
		List subModules = _md.getSubModules();

		if (subModules != null) {
			for (Iterator i = subModules.iterator(); i.hasNext();) {
				SubModuleDescriptor smd = (SubModuleDescriptor) i.next();

				Element subModule = getSubModuleElement(smd);

				module.add(subModule);
			}
		}
	}

	private Element getDependencyElement(DependencyDescriptor dd) {

		Element dependency = DocumentHelper.createElement("dependency");

		dependency.addAttribute("module-id", dd.getModuleId());
		dependency.addAttribute("version", dd.getVersion());

		return dependency;
	}

	private Element getServicePointElement(ServicePointDescriptor spd) {
		Element servicePoint = DocumentHelper.createElement("service-point");

		servicePoint.addAttribute("id", qualify(spd.getId()));
		servicePoint.addAttribute("interface", spd.getInterfaceClassName());
		if (spd.getVisibility() == Visibility.PRIVATE)
			servicePoint.addAttribute("visibility", "private");
		if (spd.getParametersCount() != Occurances.REQUIRED)
			servicePoint.addAttribute("parameters-occurs", spd
					.getParametersCount().getName().toLowerCase());

		if (spd.getAnnotation() != null) {
			servicePoint.addText(spd.getAnnotation());
		}

		if (spd.getParametersSchema() != null)
			addSchema(servicePoint, (SchemaImpl) spd.getParametersSchema(),
					"parameters-schema");
		else if (spd.getParametersSchemaId() != null)
			servicePoint.addAttribute("parameters-schema-id", qualify(spd
					.getParametersSchemaId()));

		InstanceBuilder ib = spd.getInstanceBuilder();

		if (ib != null) {
			Element instanceBuilder = getInstanceBuilderElement(ib);

			servicePoint.add(instanceBuilder);
		}

		List interceptors = spd.getInterceptors();

		if (interceptors != null) {
			for (Iterator i = interceptors.iterator(); i.hasNext();) {
				InterceptorDescriptor icd = (InterceptorDescriptor) i.next();

				Element interceptor = getInterceptorElement(icd);

				servicePoint.add(interceptor);
			}
		}

		return servicePoint;
	}

	private Element getConfigurationPointElement(
			ConfigurationPointDescriptor cpd) {
		Element configurationPoint = DocumentHelper
				.createElement("configuration-point");

		configurationPoint.addAttribute("id", qualify(cpd.getId()));
		if (cpd.getVisibility() == Visibility.PRIVATE)
			configurationPoint.addAttribute("visibility", "private");

		if (cpd.getAnnotation() != null) {
			configurationPoint.addText(cpd.getAnnotation());
		}
		if (cpd.getContributionsSchema() != null)
			addSchema(configurationPoint, (SchemaImpl) cpd
					.getContributionsSchema(), "schema");
		else if (cpd.getContributionsSchemaId() != null)
			configurationPoint.addAttribute("schema-id", qualify(cpd
					.getContributionsSchemaId()));

		return configurationPoint;
	}

	private Element getContributionElement(ContributionDescriptor cd) {
		Element contribution = DocumentHelper.createElement("contribution");

		contribution.addAttribute("configuration-id", qualify(cd
				.getConfigurationId()));

		if (cd.getConditionalExpression() != null)
			contribution.addAttribute("if", cd.getConditionalExpression());

		List parameters = cd.getElements();

		if (parameters != null) {
			for (Iterator i = parameters.iterator(); i.hasNext();) {
				org.apache.hivemind.Element parameter = (org.apache.hivemind.Element) i
						.next();

				Element element = getParamterElement(parameter);

				contribution.add(element);
			}
		}

		if (cd.getAnnotation() != null) {
			contribution.addText(cd.getAnnotation());
		}

		return contribution;
	}

	private Element getImplementationElement(ImplementationDescriptor id) {
		Element implementation = DocumentHelper.createElement("implementation");

		implementation.addAttribute("service-id", qualify(id.getServiceId()));

		if (id.getConditionalExpression() != null)
			implementation.addAttribute("if", id.getConditionalExpression());

		if (id.getAnnotation() != null) {
			implementation.addText(id.getAnnotation());
		}

		InstanceBuilder ib = id.getInstanceBuilder();

		if (ib != null) {
			Element instanceBuilder = getInstanceBuilderElement(ib);

			implementation.add(instanceBuilder);
		}

		List interceptors = id.getInterceptors();

		if (interceptors != null) {
			for (Iterator i = interceptors.iterator(); i.hasNext();) {
				InterceptorDescriptor icd = (InterceptorDescriptor) i.next();

				Element interceptor = getInterceptorElement(icd);

				implementation.add(interceptor);
			}
		}

		return implementation;
	}

	private Element getSubModuleElement(SubModuleDescriptor smd) {
		Element subModule = DocumentHelper.createElement("sub-module");

		subModule.addAttribute("descriptor", smd.getDescriptor().getPath());

		return subModule;
	}

	private Element getInstanceBuilderElement(InstanceBuilder ib) {
		Element instanceBuilder;

		if (ib instanceof CreateInstanceDescriptor) {
			CreateInstanceDescriptor cid = (CreateInstanceDescriptor) ib;
			instanceBuilder = DocumentHelper.createElement("create-instance");

			instanceBuilder.addAttribute("class", cid.getInstanceClassName());
			if (!cid.getServiceModel().equals("singleton"))
				instanceBuilder.addAttribute("model", cid.getServiceModel());
		} else {
			InvokeFactoryDescriptor ifd = (InvokeFactoryDescriptor) ib;
			instanceBuilder = DocumentHelper.createElement("invoke-factory");

			if (!ifd.getFactoryServiceId().equals("hivemind.BuilderFactory"))
				instanceBuilder.addAttribute("service-id", qualify(ifd
						.getFactoryServiceId()));
			if (ifd.getServiceModel() != null)
				instanceBuilder.addAttribute("model", ifd.getServiceModel());

			List parameters = ifd.getParameters();

			if (parameters != null) {
				for (Iterator i = parameters.iterator(); i.hasNext();) {
					org.apache.hivemind.Element parameter = (org.apache.hivemind.Element) i
							.next();

					Element element = getParamterElement(parameter);

					instanceBuilder.add(element);
				}
			}
		}

		return instanceBuilder;
	}

	private Element getInterceptorElement(InterceptorDescriptor icd) {
		Element interceptor = DocumentHelper.createElement("interceptor");

		interceptor.addAttribute("service-id", qualify(icd
				.getFactoryServiceId()));
		if (icd.getBefore() != null)
			interceptor.addAttribute("before", icd.getBefore());
		if (icd.getAfter() != null)
			interceptor.addAttribute("after", icd.getAfter());
		return interceptor;
	}

	private Element getParamterElement(org.apache.hivemind.Element parameter) {
		Element element = DocumentHelper.createElement(parameter
				.getElementName());

		List attributes = parameter.getAttributes();

		for (Iterator i = attributes.iterator(); i.hasNext();) {
			Attribute attribute = (Attribute) i.next();

			element.addAttribute(attribute.getName(), attribute.getValue());
		}

		List elements = parameter.getElements();

		for (Iterator i = elements.iterator(); i.hasNext();) {
			org.apache.hivemind.Element nestedParameter = (org.apache.hivemind.Element) i
					.next();

			element.add(getParamterElement(nestedParameter));
		}

		return element;
	}

	private void addSchema(Element container, SchemaImpl s, String elementName) {
		if (_processedSchemas.contains(s))
			return;

		Element schema = DocumentHelper.createElement(elementName);

		if (s.getId() != null)
			schema.addAttribute("id", qualify(s.getId()));

		if (s.getVisibility() == Visibility.PRIVATE)
			schema.addAttribute("visibility", "private");

		if (s.getAnnotation() != null) {
			schema.addText(s.getAnnotation());
		}

		for (Iterator j = s.getElementModel().iterator(); j.hasNext();) {
			ElementModel em = (ElementModel) j.next();

			Element element = getElementElement(em);

			schema.add(element);
		}

		container.add(schema);

		_processedSchemas.add(s);
	}

	private Element getRulesElement(ElementModel em) {
		Element rules = DocumentHelper.createElement("rules");

		for (Iterator i = em.getRules().iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();

			Element rule = null;

			if (r instanceof CreateObjectRule) {
				CreateObjectRule cor = (CreateObjectRule) r;
				rule = DocumentHelper.createElement("create-object");

				rule.addAttribute("class", cor.getClassName());
			} else if (r instanceof InvokeParentRule) {
				InvokeParentRule ipr = (InvokeParentRule) r;
				rule = DocumentHelper.createElement("invoke-parent");

				rule.addAttribute("method", ipr.getMethodName());
				if (ipr.getDepth() != 1)
					rule
							.addAttribute("depth", Integer.toString(ipr
									.getDepth()));
			} else if (r instanceof PushAttributeRule) {
				PushAttributeRule par = (PushAttributeRule) r;
				rule = DocumentHelper.createElement("push-attribute");

				rule.addAttribute("attribute", par.getAttributeName());
			} else if (r instanceof PushContentRule) {
				rule = DocumentHelper.createElement("push-content");
			} else if (r instanceof ReadAttributeRule) {
				ReadAttributeRule rar = (ReadAttributeRule) r;
				rule = DocumentHelper.createElement("read-attribute");

				rule.addAttribute("property", rar.getPropertyName());
				rule.addAttribute("attribute", rar.getAttributeName());
				if (!rar.getSkipIfNull())
					rule.addAttribute("skip-if-null", "false");
				if (rar.getTranslator() != null)
					rule.addAttribute("translator", rar.getTranslator());
			} else if (r instanceof ReadContentRule) {
				ReadContentRule rcr = (ReadContentRule) r;
				rule = DocumentHelper.createElement("read-content");

				rule.addAttribute("property", rcr.getPropertyName());
			} else if (r instanceof SetModuleRule) {
				SetModuleRule smr = (SetModuleRule) r;
				rule = DocumentHelper.createElement("set-module");

				rule.addAttribute("property", smr.getPropertyName());
			} else if (r instanceof SetParentRule) {
				SetParentRule spr = (SetParentRule) r;
				rule = DocumentHelper.createElement("set-parent");

				rule.addAttribute("property", spr.getPropertyName());
			} else if (r instanceof SetPropertyRule) {
				SetPropertyRule spr = (SetPropertyRule) r;
				rule = DocumentHelper.createElement("set-property");

				rule.addAttribute("property", spr.getPropertyName());
				rule.addAttribute("value", spr.getValue());
			} else if (r instanceof ConversionDescriptor) {
				ConversionDescriptor cd = (ConversionDescriptor) r;
				rule = DocumentHelper.createElement("conversion");

				rule.addAttribute("class", cd.getClassName());
				if (!cd.getParentMethodName().equals("addElement"))
					rule
							.addAttribute("parent-method", cd
									.getParentMethodName());

				for (Iterator j = cd.getAttributeMappings().iterator(); j
						.hasNext();) {
					AttributeMappingDescriptor amd = (AttributeMappingDescriptor) j
							.next();

					Element map = DocumentHelper.createElement("map");

					map.addAttribute("attribute", amd.getAttributeName());
					map.addAttribute("property", amd.getPropertyName());

					rule.add(map);
				}
			} else {
				rule = DocumentHelper.createElement("custom");

				rule.addAttribute("class", r.getClass().getName());
			}

			if (rule != null)
				rules.add(rule);
		}
		return rules;
	}

	private Element getElementElement(ElementModel em) {
		Element element = DocumentHelper.createElement("element");
		element.addAttribute("name", em.getElementName());

		if (em.getAnnotation() != null) {
			element.addText(em.getAnnotation());
		}

		for (Iterator i = em.getAttributeModels().iterator(); i.hasNext();) {
			AttributeModel am = (AttributeModel) i.next();

			Element attribute = getAttributeElement(am);

			element.add(attribute);
		}

		for (Iterator i = em.getElementModel().iterator(); i.hasNext();) {
			ElementModel nestedEm = (ElementModel) i.next();

			Element nestedElement = getElementElement(nestedEm);

			element.add(nestedElement);
		}

		if (!em.getRules().isEmpty()) {
			Element rules = getRulesElement(em);

			element.add(rules);
		}

		return element;
	}

	private Element getAttributeElement(AttributeModel am) {
		Element attribute = DocumentHelper.createElement("attribute");

		attribute.addAttribute("name", am.getName());
		if (am.isRequired())
			attribute.addAttribute("required", "true");
		if (am.isUnique())
			attribute.addAttribute("unique", "true");
		if (!am.getTranslator().equals("smart"))
			attribute.addAttribute("translator", am.getTranslator());

		if (am.getAnnotation() != null) {
			attribute.addText(am.getAnnotation());
		}

		return attribute;
	}

	private String qualify(String id) {
		return IdUtils.qualify(_md.getModuleId(), id);
	}

	private DocumentBuilder getBuilder() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setIgnoringComments(true);

		try {
			return factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	public static Document createDefaultRegistryDocument() {
		ClassResolver resolver = new DefaultClassResolver();
		ModuleDescriptorProvider provider = new XmlModuleDescriptorProvider(
				resolver);

		Dom4JRegistrySerializer serializer = new Dom4JRegistrySerializer();

		serializer.addModuleDescriptorProvider(provider);

		return serializer.createRegistryDocument();
	}
}
