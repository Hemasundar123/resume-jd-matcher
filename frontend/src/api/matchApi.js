const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

/**
 * Parse an error response from the backend's ErrorResponse DTO shape
 * ({ status, error, message, details, timestamp }) into a readable string.
 */
async function parseError(response) {
  try {
    const body = await response.json();
    if (body.details && body.details.length > 0) {
      return `${body.message}: ${body.details.join(', ')}`;
    }
    return body.message || `Request failed with status ${response.status}`;
  } catch {
    return `Request failed with status ${response.status}`;
  }
}

/** POST /api/v1/match — resume text + JD text → MatchResponse */
export async function matchFromText({ resumeTitle, resumeText, jdTitle, jdText }) {
  const response = await fetch(`${BASE_URL}/match`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ resumeTitle, resumeText, jdTitle, jdText }),
  });

  if (!response.ok) {
    throw new Error(await parseError(response));
  }
  return response.json();
}

/** POST /api/v1/match/upload — resume PDF + JD text (multipart) → MatchResponse */
export async function matchFromUpload({ resumeFile, resumeTitle, jdTitle, jdText }) {
  const formData = new FormData();
  formData.append('resume', resumeFile);
  formData.append('jdText', jdText);
  if (resumeTitle) formData.append('resumeTitle', resumeTitle);
  if (jdTitle) formData.append('jdTitle', jdTitle);

  const response = await fetch(`${BASE_URL}/match/upload`, {
    method: 'POST',
    body: formData, // browser sets multipart boundary automatically
  });

  if (!response.ok) {
    throw new Error(await parseError(response));
  }
  return response.json();
}

/** GET /api/v1/match/{id} — retrieve a stored report */
export async function getMatchReport(id) {
  const response = await fetch(`${BASE_URL}/match/${id}`);
  if (!response.ok) {
    throw new Error(await parseError(response));
  }
  return response.json();
}