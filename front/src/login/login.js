import "../../kubys_favicon.ico";
import './login.scss';

import firebase from "firebase/app";
import 'firebase/auth';

const firebaseConfig = {
    apiKey: "AIzaSyDmXTP9dZiqvzc2o1d0VREobnx4sFVduxY",
    authDomain: "kubys-id.firebaseapp.com",
    databaseURL: "https://kubys-id.firebaseio.com",
    projectId: "kubys-id",
    storageBucket: "kubys-id.appspot.com",
    messagingSenderId: "no-reply@kubys.fr",
    appID: "kubys-id",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

firebase.auth().signOut();
/**
 * Handles the sign in button press.
 */
function toggleLogIn() {
    let email = document.getElementById('email-log-in').value;
    let password = document.getElementById('password-log-in').value;
    if (email.length < 4) {
        alert('Please enter a valid email address.');
        return;
    }
    if (password.length < 4) {
        alert('Please enter a valid password.');
        return;
    }
    // Sign in with email and pass.
    firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
        // Handle Errors here.
        error.code === 'auth/wrong-password'? alert('Wrong password.') : alert(error.message);

        console.error(error);
        document.getElementById('quickstart-log-in').disabled = false;
    });
    document.getElementById('quickstart-log-in').disabled = true;
}

/**
 * Handles the sign up button press.
 */
function handleSignUp() {
    let name = document.getElementById('name-sign-up').value;
    let email = document.getElementById('email-sign-up').value;
    let password = document.getElementById('password-sign-up').value;
    if (email.length < 4) {
        alert('Please enter an email address.');
        return;
    }
    if (password.length < 4) {
        alert('Please enter a password.');
        return;
    }
    if (name.length < 4) {
        alert('Please enter a name.');
        return;
    }
    // Sign in with email and pass.
    firebase.auth().createUserWithEmailAndPassword(email, password).then(function() {
        let user = firebase.auth().currentUser;
        user.updateProfile({
            displayName: name,
        }).then(function() {
            // Update successful.
        }).catch(function(error) {
            // An error happened.
        });
    }).catch(function(error) {
        // Handle Errors here.
        let errorCode = error.code;
        let errorMessage = error.message;
        if (errorCode === 'auth/weak-password') {
            alert('The password is too weak.');
        } else {
            alert(errorMessage);
        }
        console.log(error);
    });
}
/**
 * Sends an email verification to the user.
 */
function sendEmailVerification() {
    firebase.auth().currentUser.sendEmailVerification().then(function() {
        alert('Email Verification Sent!');
    });
}

function sendPasswordReset() {
    let email = document.getElementById('email').value;
    firebase.auth().sendPasswordResetEmail(email).then(function() {
        // Password Reset Email Sent!
        alert('Password Reset Email Sent!');
    }).catch(function(error) {
        // Handle Errors here.
        let errorCode = error.code;
        let errorMessage = error.message;
        if (errorCode === 'auth/invalid-email') {
            alert(errorMessage);
        } else if (errorCode === 'auth/user-not-found') {
            alert(errorMessage);
        }
        console.log(error);
    });
}
/**
 * initApp handles setting up UI event listeners and registering Firebase auth listeners:
 *  - firebase.auth().onAuthStateChanged: This listener is called when the user is signed in or
 *    out, and that is where we update the UI.
 */
function initApp() {
    // Listening for auth state changes.
    firebase.auth().onAuthStateChanged(function(user) {
        document.getElementById('quickstart-verify-email').disabled = true;
        if (user) {
            // User is signed in.
            let displayName = user.displayName;
            let email = user.email;
            let emailVerified = user.emailVerified;
            let photoURL = user.photoURL;
            let isAnonymous = user.isAnonymous;
            let uid = user.uid;
            let providerData = user.providerData;
            document.location.href = "/";
        } else {
            // User is signed out.
            if(document.location.pathname !== "/login.html") document.location.href = "/login.html";
        }
    });
    document.getElementById('quickstart-log-in').addEventListener('click', toggleLogIn, false);
    document.getElementById('quickstart-sign-up').addEventListener('click', handleSignUp, false);
    document.getElementById('quickstart-verify-email').addEventListener('click', sendEmailVerification, false);
    document.getElementById('quickstart-password-reset').addEventListener('click', sendPasswordReset, false);
}
function addEvent() {
    const loginBtn = document.getElementById('log-in-slider');
    const logInParentClassList = loginBtn.parentNode.parentNode.classList;
    const signupBtn = document.getElementById('sign-up-slider');
    const signUpParentClassList = signupBtn.parentNode.parentNode.classList;

    loginBtn.addEventListener('click', (e) => {
        Array.from(logInParentClassList).find((element) => {
            if(element === "slide-up") {
                logInParentClassList.remove('slide-up');
                signUpParentClassList.add('slide-up');
            }
        });
    });

    signupBtn.addEventListener('click', (e) => {
        Array.from(signUpParentClassList).find((element) => {
            if(element === "slide-up") {
                logInParentClassList.add('slide-up');
                signUpParentClassList.remove('slide-up');
            }
        });
    });
}
window.onload = function() {
    initApp();
    addEvent();
};