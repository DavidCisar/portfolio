export interface IUser {
    id? : number;
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    role?: string;
    authorities?: string[];
}
