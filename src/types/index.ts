// TypeScript types for Desk Audit Frontend - matches backend API DTOs

export type DeskAuditStatus = 'OPEN' | 'EVIDENCE_GATHERING' | 'DOCUMENTS_REQUESTED' | 'SAMPLING_DETERMINED' | 'FINDINGS_RECORDED' | 'DRAFT_REPORT_SUBMITTED' | 'FINALIZED' | 'ESCALATED_TO_COMPREHENSIVE' | 'SUSPENDED_FRAUD_INVESTIGATION'

export type Severity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

export type TeamLeaderDecision = 'APPROVE_FINALIZE' | 'ESCALATE_COMPREHENSIVE'

export type SamplingMethod = 'RANDOM' | 'STRATIFIED' | 'JUDGMENTAL' | 'STATISTICAL'

export interface ApiResponse<T> {
  success: boolean
  data: T
  error: string | null
}

export interface DeskAuditResponse {
  id: string
  auditCaseId: string
  tin: string
  status: DeskAuditStatus
  currentStage: string
  evidence: EvidenceItem[]
  documentRequests: DocumentRequest[]
  findings: DeskAuditFinding[]
  sampleSelection: SampleSelection | null
  draftReport: DraftAuditReport | null
  teamLeaderDecision: string | null
  fraudFlag: FraudFlag | null
  createdAt: string
  updatedAt: string
}

export interface EvidenceItem {
  id: string
  title: string
  source: string
  category: string
  status: string
  date: string
}

export interface DocumentRequest {
  id: string
  documentType: string
  status: string
  requestedAt: string
}

export interface DeskAuditFinding {
  id: string
  area: string
  severity: Severity
  description: string
  requiresRiskUpdate: boolean
}

export interface SampleSelection {
  method: SamplingMethod
  criteria: string
  selectedItems: string[]
  sampleSize: number
}

export interface DraftAuditReport {
  narrative: string
  findingsSummary: string
  preparedByActorId: string
  preparedAt: string
}

export interface FraudFlag {
  indicatorNotes: string
  flaggedByActorId: string
  flaggedAt: string
}

export interface StartDeskAuditRequest {
  auditCaseId: string
  tin: string
}

export interface GatherEvidenceRequest {
  actorId: string
}

export interface RequestDocumentsRequest {
  documentTypes: string[]
  requestedByActorId: string
}

export interface DetermineSamplingRequest {
  method: SamplingMethod
  criteria: string
  selectedItems: string[]
  sampleSize: number
}

export interface RecordFindingsRequest {
  findings: Array<{
    area: string
    severity: Severity
    description: string
    requiresRiskUpdate: boolean
  }>
}

export interface SubmitDraftReportRequest {
  narrative: string
  findingsSummary: string
  preparedByActorId: string
}

export interface TeamLeaderDecisionRequest {
  decision: TeamLeaderDecision
  actorId: string
  narrative: string
}

export interface FlagFraudRequest {
  indicatorNotes: string
  flaggedByActorId: string
}