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
        <h1>🤖 Resume ↔ JD Matcher</h1>
        <p>Semantic scoring + AI feedback, powered by pgvector and a local/Claude chat model.</p>
      </header>

      <main>
        {status === 'loading' && <Loading label="Extracting, embedding, and analyzing…" />}

        {status === 'error' && (
          <div className="error-card">
            <p>{error}</p>
            <button onClick={reset} className="secondary">Try Again</button>
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