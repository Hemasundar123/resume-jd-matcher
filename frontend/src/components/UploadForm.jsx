import { useState } from 'react';

const MODES = { TEXT: 'text', UPLOAD: 'upload' };

export default function UploadForm({ onSubmitText, onSubmitUpload, disabled }) {
  const [mode, setMode] = useState(MODES.TEXT);
  const [resumeTitle, setResumeTitle] = useState('');
  const [resumeText, setResumeText] = useState('');
  const [resumeFile, setResumeFile] = useState(null);
  const [jdTitle, setJdTitle] = useState('');
  const [jdText, setJdText] = useState('');
  const [formError, setFormError] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    setFormError('');

    if (!jdText.trim()) {
      setFormError('Job description text is required.');
      return;
    }

    if (mode === MODES.TEXT) {
      if (!resumeText.trim()) {
        setFormError('Resume text is required.');
        return;
      }
      onSubmitText({ resumeTitle, resumeText, jdTitle, jdText });
    } else {
      if (!resumeFile) {
        setFormError('Choose a resume PDF.');
        return;
      }
      onSubmitUpload({ resumeFile, resumeTitle, jdTitle, jdText });
    }
  }

  return (
    <form className="upload-form" onSubmit={handleSubmit}>
      <div className="scan-frame">
        <div className="panel-grid">
          {/* SOURCE A — RESUME */}
          <div className="source-panel">
            <span className="source-tag"><b>Source A</b> — Resume</span>

            <div className="tabs">
              <button type="button" className={mode === MODES.TEXT ? 'tab active' : 'tab'} onClick={() => setMode(MODES.TEXT)}>
                Paste text
              </button>
              <button type="button" className={mode === MODES.UPLOAD ? 'tab active' : 'tab'} onClick={() => setMode(MODES.UPLOAD)}>
                Upload PDF
              </button>
            </div>

            <div className="field">
              <label>Title (optional)</label>
              <input type="text" value={resumeTitle} onChange={(e) => setResumeTitle(e.target.value)} placeholder="e.g. Jane Doe Resume" />
            </div>

            {mode === MODES.TEXT ? (
              <div className="field">
                <label>Resume text</label>
                <textarea rows={9} value={resumeText} onChange={(e) => setResumeText(e.target.value)} placeholder="Paste resume content…" />
              </div>
            ) : (
              <div className="field">
                <label>Resume PDF</label>
                <input type="file" accept="application/pdf" onChange={(e) => setResumeFile(e.target.files?.[0] ?? null)} />
                {resumeFile && <span className="file-name">{resumeFile.name}</span>}
              </div>
            )}
          </div>

          {/* CONNECTOR */}
          <div className="connector">
            <div className="beam" />
            <div className="glyph">⇌</div>
            <div className="beam" />
          </div>

          {/* SOURCE B — JD */}
          <div className="source-panel">
            <span className="source-tag"><b>Source B</b> — Job description</span>

            <div className="field">
              <label>Title (optional)</label>
              <input type="text" value={jdTitle} onChange={(e) => setJdTitle(e.target.value)} placeholder="e.g. Backend Engineer JD" />
            </div>

            <div className="field">
              <label>Job description text</label>
              <textarea rows={9} value={jdText} onChange={(e) => setJdText(e.target.value)} placeholder="Paste job description…" />
            </div>
          </div>
        </div>
      </div>

      {formError && <p className="form-error">{formError}</p>}

      <button type="submit" className="primary" disabled={disabled}>
        {disabled ? 'Scanning…' : 'Run scan →'}
      </button>
    </form>
  );
}
