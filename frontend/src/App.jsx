import { useState } from 'react';
import UploadForm from './components/UploadForm.jsx';
import MatchResult from './components/MatchResult.jsx';
import Loading from './components/Loading.jsx';
import { matchFromText, matchFromUpload } from './api/matchApi.js';

export default function App() {
  const [status, setStatus] = useState('idle'); // idle | loading | done | error
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

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

  return (
    <div className="app">
      <header className="app-header">
        <div className="eyebrow"><span className="dot" /> Semantic Analysis Engine</div>
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

        {(status === 'idle' || status === 'error') && status !== 'loading' && (
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
