import { useEffect, useState } from 'react';

const STEPS = [
  'Extracting document text…',
  'Generating embeddings…',
  'Computing similarity…',
  'Consulting the model…',
];

export default function Loading() {
  const [step, setStep] = useState(0);

  useEffect(() => {
    const id = setInterval(() => {
      setStep((s) => (s + 1 < STEPS.length ? s + 1 : s));
    }, 3500);
    return () => clearInterval(id);
  }, []);

  return (
    <div className="loading">
      <div className="scan-bar" />
      <p className="status-line"><b>{STEPS[step]}</b></p>
    </div>
  );
}
