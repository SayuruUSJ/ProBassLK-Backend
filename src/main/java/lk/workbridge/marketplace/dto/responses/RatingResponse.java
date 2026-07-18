package lk.workbridge.marketplace.dto.responses;

public record RatingResponse(

      int stars,
       String comment,
         String workerId,
        String workerName,
         String  clientId,
          String clientName
) {
}
