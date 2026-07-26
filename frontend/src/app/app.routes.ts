import { Routes } from '@angular/router';

import { AnalysisFormComponent } from './features/analysis/pages/analysis-form/analysis-form.component';
import { BusinessPlanComponent } from './features/business-plan/business-plan.component';
import { BusinessPlanRedirectComponent } from './features/business-plan/business-plan-redirect/business-plan-redirect.component';
import { AnalysisResultComponent } from './features/analysis/pages/analysis-result/analysis-result.component';
export const routes: Routes = [
	{
		path: '',
		component: AnalysisFormComponent
	},
	{
		path: 'business-plan',
		component: BusinessPlanComponent
	},
	{
   		 path:'analysis-result',
   		 component:AnalysisResultComponent
	},
	{
   		 path:'business-plan-redirect',
   		 component: BusinessPlanRedirectComponent
	},
	{
		path: '**',
		redirectTo: ''
	}
];
