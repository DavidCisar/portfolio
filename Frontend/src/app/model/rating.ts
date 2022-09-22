import { IUser } from "./user";

export interface IRating {
  id?: number;
  stars?: string;
  name: string
  message: string;
  link: string;
  isAccepted?: boolean;
}
