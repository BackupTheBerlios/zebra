
/**
 * Submit a from with a new action
**/
function submitFormNewAction(formId, action) {
	var f = document.getElementById(formId)
	f.action = action
	f.submit()
}

/**
 * Allows you to submit a form without a button
**/
function submitForm(formId) {
	var f = document.getElementById(formId)	
	f.submit()
}

/**
 * Allows you to change the action of a form
**/
function changeFormAction(formId, action) {
	var f = document.getElementById(formId)	
	f.action = action
}

/**
 * Allows you to submit a form without a button
**/
function changeValue(elId, newvalue) {
	var e = document.getElementById(elId)	
	e.value=newvalue;
}

/**
 * Pop up a picture in a new window
**/
function popUpPicture(url, title) {
  	window.open(url, "test", 'toolbar=0,scrollbars=1,location=0,statusbar=0,menubar=0,resizable=1,width=800,height=600,left = 112,top = 50');
}

/**
 *
 * POPUP HELP SCRIPT 
 * NEED TO MAKE THIS x-BROWSER COMPATIBLE
 *
**/

function showElement(elId, linkEl) {
	return positionElement(elId, getRightEdge(linkEl) + 5, getTopEdge(linkEl));
}

function hideElement(elId, linkEl) {
	return positionElement(elId, -9999, -9999);
}

function positionElement(elId, left, top) {
	if (document.getElementById) {
		var el = document.getElementById(elId);
		el.style.left = left + "px";
		el.style.top = top + "px";
	}
	return false;
}

function getLeftEdge(el) {
	/*var iLeft = 0;
	while (el.parentNode != null) {
		iLeft = iLeft + parseInt(el.offsetLeft);
		el = el.parentNode;
	}
	return iLeft;*/
	return el.offsetLeft;
}

function getTopEdge(el) {
	/*var iTop = 0;
	while (el.parentNode != null) {
		iTop += parseInt(el.offsetTop);
		el = el.parentNode;
		alert(iTop)
	}	
	return iTop;*/
	return el.offsetTop;
}

function getRightEdge(el) {
	return getLeftEdge(el) + parseInt(el.offsetWidth);
}
