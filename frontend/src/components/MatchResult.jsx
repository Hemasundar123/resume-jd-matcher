import SimilarityGauge from './SimilarityGauge.jsx';

export default function MatchResult({ result, onReset }) {
  return (
    <div className="result-card">
      <div className="result-header">
        <h2>Match Report #{result.id}</h2>
        <button onClick={onReset} className="secondary">
          New Match
        </button>
      </div>

      <SimilarityGauge percent={result.similarityPercent} />

      <div className="feedback-grid">
        <FeedbackBlock title="✅ Matching Skills" text={result.matchingSkills} />
        <FeedbackBlock title="⚠️ Gaps" text={result.gaps} />
        <FeedbackBlock title="💡 Suggestions" text={result.suggestions} />
      </div>

      <p className="timestamp">
        Generated {new Date(result.createdAt).toLocaleString()}
      </p>
    </div>
  );
}

function FeedbackBlock({ title, text }) {
  return (
    <div className="feedback-block">
      <h3>{title}</h3>
      <p>{text || '—'}</p>
    </div>
  );
}