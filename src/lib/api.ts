import axios from 'axios'
import type {
  ApiResponse,
  DeskAuditResponse,
  StartDeskAuditRequest,
  GatherEvidenceRequest,
  RequestDocumentsRequest,
  DetermineSamplingRequest,
  RecordFindingsRequest,
  SubmitDraftReportRequest,
  TeamLeaderDecisionRequest,
  FlagFraudRequest,
} from '@/types'

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use((config) => {
  const actorId = localStorage.getItem('actorId') || 'auditor-001'
  config.headers['X-Actor-Id'] = actorId
  return config
})

const unwrap = <T>(response: { data: ApiResponse<T> }): T => response.data.data

export const deskAuditApi = {
  start: (request: StartDeskAuditRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>('/desk-audits', request).then(unwrap),

  gatherEvidence: (id: string, request: GatherEvidenceRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/evidence`, request).then(unwrap),

  requestDocuments: (id: string, request: RequestDocumentsRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/document-requests`, request).then(unwrap),

  determineSampling: (id: string, request: DetermineSamplingRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/sampling`, request).then(unwrap),

  recordFindings: (id: string, request: RecordFindingsRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/findings`, request).then(unwrap),

  submitDraftReport: (id: string, request: SubmitDraftReportRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/draft-report`, request).then(unwrap),

  teamLeaderDecision: (id: string, request: TeamLeaderDecisionRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/team-leader-decision`, request).then(unwrap),

  flagFraud: (id: string, request: FlagFraudRequest) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/fraud-flag`, request).then(unwrap),

  finalize: (id: string, finalizedByActorId: string) =>
    api.post<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}/finalize`, finalizedByActorId, {
      headers: { 'Content-Type': 'text/plain' },
    }).then(unwrap),

  getById: (id: string) =>
    api.get<ApiResponse<DeskAuditResponse>>(`/desk-audits/${id}`).then(unwrap),
}

export default api