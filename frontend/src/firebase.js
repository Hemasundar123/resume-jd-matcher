import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider, signInWithPopup, signOut, onAuthStateChanged } from 'firebase/auth';

// Paste your config from Firebase Console → Project Settings → Your apps
const firebaseConfig = {
  apiKey: "AIzaSyANACNgxWW9ShgbuWRmf4k_4QDfNiHmXtE",
  authDomain: "resume-ats-61056.firebaseapp.com",
  projectId: "resume-ats-61056",
  storageBucket: "resume-ats-61056.firebasestorage.app",
  messagingSenderId: "291718510142",
  appId: "1:291718510142:web:293a6d44d931a1abd0836f",
  measurementId: "G-0S15YCJ8S6"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);

const googleProvider = new GoogleAuthProvider();

export function login() {
  return signInWithPopup(auth, googleProvider);
}

export function logout() {
  return signOut(auth);
}

export function watchAuthState(callback) {
  return onAuthStateChanged(auth, callback);
}
