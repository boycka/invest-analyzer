export interface AnalysisResponse {

  id: number;

  analysisSummary: string;

  viabilityScore: number;

  recommendation: string;

  dimensions: AnalysisDimension[];

  risks: AnalysisRisk[];

  recommendations: AnalysisRecommendation[];

  generationSource: string;

}

export interface AnalysisDimension {
  dimension: string;
  score: number;
  explanation: string;
}

export interface AnalysisRisk {
  description: string;
  criticality: string;
  mitigation: string;
}

export interface AnalysisRecommendation {
  title: string;
  description: string;
  priority: number;
}
