package notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA9OXFGRA:APA91bE5u4pue4MFns7owGFKN6PYxtX-sj0WE7KMQLfEz5wYsUqWA6p-FOJ4hrXmdQ1yqg_JRf7H-3a6keTEiSUZAE3tRHY7n1VIohSEmf3_bZjkzRZmV1GLLT9xWeANvN2hUTy-uclz"
    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
