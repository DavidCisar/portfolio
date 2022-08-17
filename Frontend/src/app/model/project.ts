import { ILanguage } from './language';
import { IFramework } from './framework';
import { ITopic } from './topic';

export interface IProject {
  id: number;
  name: string;
  description: string;
  languages: ILanguage[];
  frameworks: IFramework[];
  topic: ITopic[];

}
