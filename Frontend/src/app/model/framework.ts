import { ILanguage } from "./language";

export interface IFramework {

    id: number;
    name: string;
    description: string;
    version: string;
    language: ILanguage;

}