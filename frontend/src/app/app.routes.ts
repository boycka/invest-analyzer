import { Routes } from '@angular/router';

import { AnalysisFormComponent } from './features/analysis-form/analysis-form.component';
import { BusinessPlanComponent } from './features/business-plan/business-plan.component';

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
		path: '**',
		redirectTo: ''
	}
];
