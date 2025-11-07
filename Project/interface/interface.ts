export interface User{
    id: number;
    fullName: string;
    email: string;
    password: string;
    gender: boolean;
    phoneNumber: string;
    dateOfBirth: Date;
}

export interface UserRequest extends Omit<User, "id"> {}

export interface UserLogin extends Pick<User, "email" | "password"> {}