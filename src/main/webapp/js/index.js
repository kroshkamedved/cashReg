/**
 * method for input validation
 * @returns {boolean}
 */
function empty() {
    var x;
    x = document.getElementById("prod_identifier").value;
    var numeric_alpha = /^(?=.*[a-zA-Z])(?=.*[0-9])/;
    if (x == "" || numeric_alpha.test(x)) {
        alert("Enter a valid Id or Name");
        return false;
    }
}

function validate() {
    var x;
    x = document.getElementById("prod_name").value;
    var numeric_alpha = /^[a-z0-9]+$/i;
    if (numeric_alpha.test(x)) {
    } else {
        alert("Enter a valid name");
        return false;
    }
}

/**
 * change "edit" boolean
 */
function change() {
    var edit;
    edit = document.getElementById("edit");
    edit = !edit;
}