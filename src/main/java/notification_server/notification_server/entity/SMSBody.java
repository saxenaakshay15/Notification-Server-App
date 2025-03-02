package notification_server.notification_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SMSBody {
    private String deliverychannel;
    private Channels channels;
    private ArrayList<DestinationInfo> destination;

    public SMSBody(SMS sms){
        this.setDeliverychannel("sms");

        // Channel
        Channels channel = new Channels();
        // SMSText
        SMSText smsText = new SMSText();
        smsText.setText(sms.getMessage());
        channel.setSms(smsText);
        this.setChannels(channel);

        // destination
        DestinationInfo destinationInfo = new DestinationInfo();
        destinationInfo.setMsisdn(new ArrayList<>(Collections.singletonList(sms.getPhoneNumber())));
        destinationInfo.setCorrelationId("correlation-id");
        this.setDestination(new ArrayList<>(Collections.singletonList(destinationInfo)));
    }
}
