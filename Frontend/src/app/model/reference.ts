export interface IReference {
  id?: number;
  name: string
  message: string;
  link: string;
  createdAt?: Date;
  isAccepted?: boolean;
}
