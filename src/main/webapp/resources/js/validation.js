const FLOAT_REGEX = /^[+-]?([0-9]*[.])?[0-9]+$/;

const element = document.getElementById('formParameters:y');

function validationFloat(strFloat) {
    return FLOAT_REGEX.test(strFloat);
}

let yIsValid = false;
let rIsValid = false;

function validateY() {

    if (!element) {
        yIsValid = false;
        return;
    }

    const errorBox = document.getElementById('y_messageError')
    const button = document.getElementById('formParameters:submit')

    if (!validationFloat(element.value)) {
        errorBox.innerText = 'y should be a number';
        button.disabled = true;
        yIsValid = false;
        return;
    }

    const y = Number.parseFloat(element.value)

    if (y < -5) {
        errorBox.innerText = 'y should be greater than -5';
        button.disabled = true;
        yIsValid = false;
        return;
    }

    if (y > 5) {
        errorBox.innerText = 'y should be less than 5';
        button.disabled = true;
        yIsValid = false;
        return;
    }

    errorBox.innerText = '';

    yIsValid = true;

    if (rIsValid) {
        button.disabled = false;
    }
}

validateY()

function validateR() {

    let someRSelected = false;

    $('#formParameters\\:rs input').each(function(index) {
        const input = this;
        if (input.checked) {
            someRSelected = true;
        }
    });

    const errorBox = document.getElementById('r_messageError')
    const button = document.getElementById('formParameters:submit')

    if (!someRSelected) {
        errorBox.innerText = 'select some r';
        button.disabled = true;
        rIsValid = false;
        return;
    }

    errorBox.innerText = '';
    rIsValid = true;

    if (yIsValid) {
        button.disabled = false;
    }
}

validateR()

function validateYClick() {

    if (!element) {
        yIsValid = false;
        return;
    }

    const errorBox = document.getElementById('y_messageError')
    const button = document.getElementById('formParameters:submit')

    if (!validationFloat(element.value)) {
        errorBox.innerText = 'y should be a number';
        button.disabled = true;
        yIsValid = false;
        return;
    }

    errorBox.innerText = '';

    yIsValid = true;

    if (rIsValid) {
        button.disabled = false;
    }

}

$('form input').on('keypress', function(e) {
    return e.which !== 13;
});