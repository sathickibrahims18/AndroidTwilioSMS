import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import static spark.Spark.*;
import spark.Spark;

import static spark.Spark.get;
import static spark.Spark.post;

public class SMSBackend
{
    public static void main(String[] args) 
	{
        TwilioRestClient client = new TwilioRestClient.Builder("ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx(USE TWILIO ACCOUNT SID)", "365xxxxxxxxxxxxxxxxxxxxxxxxxxxx(USE TWILIO AUTH TOKEN)").build();
        post("/", (req, res) -> {
            String body = req.queryParams("Body");
            String to = req.queryParams("To");
			//Use your twilio number below
            String from = "+120XXXXXXXX";

            Message message = new MessageCreator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body).create(client);

            return (message.getSid());
        });
    }
}
