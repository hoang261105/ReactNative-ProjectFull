import { Hotel } from "./hotel";
import { User } from "./interface";
import { Room } from "./room";

export interface Review {
  id: number;
  hotel: Hotel; 
  user: User; 
  room: Room; 
  rating: number; 
  comment?: string; 
  commentDate: string; 
  createdAt: string;
}