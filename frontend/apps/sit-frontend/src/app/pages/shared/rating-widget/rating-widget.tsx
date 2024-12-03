import {PropsWithChildren} from "react";
import {StarIcon} from "./star-icon";

export const RatingWidget = ({ rating = 0.0 }: PropsWithChildren<{ readonly rating?: number}>) => {
  return (
    <div style={{ display: "flex", flexDirection: "row", gap: "4px", alignItems: "center" }}>
      <StarIcon isFilled={rating >= 1.0}/>
      <StarIcon isFilled={rating >= 2.0}/>
      <StarIcon isFilled={rating >= 3.0}/>
      <StarIcon isFilled={rating >= 4.0}/>
      <StarIcon isFilled={rating >= 5.0}/>
      <span>({rating})</span>
    </div>
  )
}
