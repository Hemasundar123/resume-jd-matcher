export default function SimilarityGauge({ percent }) {
  const clamped = Math.max(0, Math.min(100, percent));
  const tone = clamped >= 70 ? '#4fd8c4' : clamped >= 45 ? '#ffb454' : '#ff6b6b';

  return (
    <div className="gauge-wrap">
      <div className="radial-gauge" style={{ '--p': clamped, '--tone': tone }}>
        <div className="radial-gauge-inner">
          <span className="value">{clamped.toFixed(0)}%</span>
          <span className="label">Semantic match</span>
        </div>
      </div>
    </div>
  );
}
