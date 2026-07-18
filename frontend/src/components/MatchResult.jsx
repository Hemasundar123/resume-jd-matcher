import SimilarityGauge from './SimilarityGauge.jsx';

export default function MatchResult({ result, onReset }) {
  return (
    <div className="result-card">
      <div className="result-header">
        <h2>Match report <span className="report-id">#{result.id}</span></h2>
        <button onClick={onReset} className="secondary">New scan</button>
      </div>

      <SimilarityGauge percent={result.similarityPercent} />

      <div className="feedback-grid">
        <div className="feedback-block matching">
          <span className="tag">Matching</span>
          <p>{result.matchingSkills || '—'}</p>
        </div>
        <div className="feedback-block gaps">
          <span className="tag">Gaps</span>
          <p>{result.gaps || '—'}</p>
        </div>
        <div className="feedback-block suggestions">
          <span className="tag">Suggestions</span>
          <p>{result.suggestions || '—'}</p>
        </div>
      </div>

      <p className="timestamp">{new Date(result.createdAt).toLocaleString()}</p>
    </div>
  );
}
