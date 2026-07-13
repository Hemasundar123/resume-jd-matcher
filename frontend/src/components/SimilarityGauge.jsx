/** Small visual bar for the cosine-similarity score. */
export default function SimilarityGauge({ percent }) {
  const clamped = Math.max(0, Math.min(100, percent));
  const tone = clamped >= 70 ? 'good' : clamped >= 45 ? 'mid' : 'low';

  return (
    <div className="gauge">
      <div className="gauge-label">
        <span>Semantic Match</span>
        <strong>{clamped.toFixed(1)}%</strong>
      </div>
      <div className="gauge-track">
        <div className={`gauge-fill gauge-${tone}`} style={{ width: `${clamped}%` }} />
      </div>
    </div>
  );
}