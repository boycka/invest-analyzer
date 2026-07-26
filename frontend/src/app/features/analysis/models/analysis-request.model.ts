export interface AnalysisRequest {

  // Projet
  sector: string;
  region: string;
  description: string;
  stage: string;

  // Finance
  initialBudget: number;
  financingSource: string;
  expectedRevenue: number;
  expectedRoiMonths: number;

  // Contexte
  experienceLevel: string;
  competitionLevel: string;
  taxAdvantages: boolean;
  freeZoneStatus: boolean;

}