import { ILanguage } from './language';
import { IFramework } from './framework';
import { ITopic } from './topic';

export interface IProject {
  id?: number;
  name: string;
  description: string[];
  summary: string;
  projectContext: string;
  website: string;
  tasks: string[];
  languagesInProject?: ILanguage[];
  frameworksInProject?: IFramework[];
  topicsInProject?: ITopic[];
}
