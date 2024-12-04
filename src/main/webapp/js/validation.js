/**
 * 
 */

// New file: /webapp/js/validation.js
function showError(elementId, message) {
    const element = document.getElementById(elementId);
    const feedback = document.getElementById(elementId + 'Feedback');
    element.classList.add('input-error');
    feedback.textContent = message;
    feedback.style.display = 'block';
}

function clearError(elementId) {
    const element = document.getElementById(elementId);
    const feedback = document.getElementById(elementId + 'Feedback');
    element.classList.remove('input-error');
    feedback.style.display = 'none';
}

// Form validation utilities
const validators = {
    email: {
        regex: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
        message: 'Please enter a valid email address'
    },
    password: {
        minLength: 8,
        requireCapital: true,
        requireNumber: true,
        requireSpecial: true
    },
    username: {
        regex: /^[a-zA-Z0-9_]{3,20}$/,
        message: 'Username must be 3-20 characters and can contain letters, numbers and underscore'
    },
    name: {
        regex: /^[a-zA-Z\s]{2,50}$/,
        message: 'Name must contain only letters and spaces'
    },
    address: {
        minLength: 5,
        message: 'Address must be at least 5 characters long'
    }
};

// Live validation handlers
function attachValidators() {
    document.querySelectorAll('input').forEach(input => {
        input.addEventListener('input', function() {
            validateField(this);
        });
    });
}

function validateField(field) {
    const fieldType = field.getAttribute('data-validate');
    const feedbackElement = document.getElementById(`${field.id}Feedback`);
    
    let isValid = true;
    let message = '';

    switch(fieldType) {
        case 'email':
            isValid = validators.email.regex.test(field.value);
            message = isValid ? '' : validators.email.message;
            break;
        case 'password':
            isValid = validatePassword(field.value);
            message = isValid ? '' : 'Password must be at least 8 characters with uppercase, number and special character';
            break;
        case 'username':
            isValid = validators.username.regex.test(field.value);
            message = isValid ? '' : validators.username.message;
            break;
        case 'name':
            isValid = validators.name.regex.test(field.value);
            message = isValid ? '' : validators.name.message;
            break;
        case 'address':
            isValid = field.value.length >= validators.address.minLength;
            message = isValid ? '' : validators.address.message;
            break;
    }

    showFieldValidation(field, feedbackElement, isValid, message);
}

function validatePassword(password) {
    return password.length >= validators.password.minLength &&
           /[A-Z]/.test(password) &&
           /[0-9]/.test(password) &&
           /[!@#$%^&*]/.test(password);
}

function validatePasswordMatch(password, confirmPassword) {
    return password === confirmPassword;
}

function showFieldValidation(field, feedbackElement, isValid, message) {
    if (isValid) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
    } else {
        field.classList.remove('is-valid');
        field.classList.add('is-invalid');
    }
    
    if (feedbackElement) {
        feedbackElement.textContent = message;
        feedbackElement.style.display = message ? 'block' : 'none';
    }
}

function showSuccessMessage(formId, message) {
    const form = document.getElementById(formId);
    const successDiv = document.createElement('div');
    successDiv.className = 'form-success-message';
    successDiv.textContent = message;
    form.appendChild(successDiv);
    successDiv.style.display = 'block';
    setTimeout(() => successDiv.style.display = 'none', 3000);
}

// Form submission validation
function validateForm(formId) {
    const form = document.getElementById(formId);
    let isValid = true;
    
    form.querySelectorAll('input[data-validate]').forEach(input => {
        if (!validateField(input)) {
            isValid = false;
        }
    });
    
    return isValid;
}

// Add password strength indicator
function updatePasswordStrength(password) {
    const strength = {
        0: "Very Weak",
        1: "Weak",
        2: "Medium",
        3: "Strong"
    };
    
    let score = 0;
    if (password.length > 6) score++;
    if (password.match(/[a-z]/) && password.match(/[A-Z]/)) score++;
    if (password.match(/\d/)) score++;
    
    return strength[score];
}

// Add live validation feedback
function addLiveValidation() {
    const inputs = document.querySelectorAll('input[required]');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            validateField(this);
        });
    });
}

// Initialize validation on page load
document.addEventListener('DOMContentLoaded', attachValidators);