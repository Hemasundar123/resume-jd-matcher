export default function Loading({ label = 'Analyzing…' }) {
  return (
    <div className="loading">
      <div className="spinner" />
      <p>{label}</p>
    </div>
  );
}