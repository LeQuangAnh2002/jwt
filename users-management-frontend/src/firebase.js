// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import {getStorage} from "firebase/storage"

const firebaseConfig = {
    apiKey: "AIzaSyCUCDk7tN4srCi4Oqhl-r-sGzGC-lbexhg",
    authDomain: "crud-img-a4538.firebaseapp.com",
    projectId: "crud-img-a4538",
    storageBucket: "crud-img-a4538.appspot.com",
    messagingSenderId: "320648822041",
    appId: "1:320648822041:web:7a9ce9a9cf979ffc469988"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const storage = getStorage(app);