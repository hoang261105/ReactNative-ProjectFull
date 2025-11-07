export type Status = "INCOMING" | "PAST" | "CANCELLED";
export interface Booking {
  id: number;
  startDate: string; 
  endDate: string; 
  adults: number;
  children: number;
  infants: number;
  paymentMethod?: string; 
  status: Status; 
}