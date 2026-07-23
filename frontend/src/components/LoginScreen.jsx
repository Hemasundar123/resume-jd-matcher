import { login } from '../firebase.js';

export default function LoginScreen() {
  return (
    <div className="login-screen">
      <div className="eyebrow"><span className="dot" /> Semantic Analysis Engine</div>
      <h1>Resume <span className="arrow">⇌</span> JD Matcher</h1>
      <p>Sign in to run a match.</p>
      <button className="primary login-btn" onClick={login}>
        Sign in with Google
      </button>
    </div>
  );
}
