import { useState, useEffect } from 'react';
import UploadForm from './components/UploadForm.jsx';
import MatchResult from './components/MatchResult.jsx';
import Loading from './components/Loading.jsx';
import LoginScreen from './components/LoginScreen.jsx';
import { watchAuthState, logout } from './firebase.js';
import { matchFromText, matchFromUpload } from './api/matchApi.js';

export default function App() {
  const [user, setUser] = useState(undefined); // undefined = checking, null = signed out
  const [status, setStatus] = useState('idle');
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const unsubscribe = watchAuthState(setUser);
    return unsubscribe;
  }, []);

  async function handleSubmitText(payload) {
    setStatus('loading');
    setError('');
    try {
      const data = await matchFromText(payload);
      setResult(data);
      setStatus('done');
    } catch (err) {
      setError(err.message);
      setStatus('error');
    }
  }

  async function handleSubmitUpload(payload) {
    setStatus('loading');
    setError('');
    try {
      const data = await matchFromUpload(payload);
      setResult(data);
      setStatus('done');
    } catch (err) {
      setError(err.message);
      setStatus('error');
    }
  }

  function reset() {
    setResult(null);
    setError('');
    setStatus('idle');
  }

  if (user === undefined) {
    return <div className="app"><p className="status-line">Checking session…</p></div>;
  }

  if (user === null) {
    return <div className="app"><LoginScreen /></div>;
  }

  return (
    <div className="app">
      <header className="app-header">
        <div className="header-row">
          <div className="eyebrow"><span className="dot" /> Semantic Analysis Engine</div>
          <button className="secondary logout-btn" onClick={logout}>
            {user.email} — Sign out
          </button>
        </div>
        <h1>Resume <span className="arrow">⇌</span> JD Matcher</h1>
        <p>Feed in two documents. Get a similarity signal and a plain-language read on the gap.</p>
      </header>

      <main>
        {status === 'loading' && <Loading />}

        {status === 'error' && (
          <div className="error-card">
            <p>{error}</p>
            <button onClick={reset} className="secondary">Run again</button>
          </div>
        )}

        {status === 'done' && result && <MatchResult result={result} onReset={reset} />}

        {(status === 'idle' || status === 'error') && (
          <UploadForm
            onSubmitText={handleSubmitText}
            onSubmitUpload={handleSubmitUpload}
            disabled={status === 'loading'}
          />
        )}
      </main>
    </div>
  );
}
