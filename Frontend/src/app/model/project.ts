import { ITechnology } from './technology';

export interface IProject {
  id: number;
  name: string;
  description: string;
  technologies: ITechnology[];
}
