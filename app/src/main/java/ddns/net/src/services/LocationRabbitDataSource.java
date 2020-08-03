package ddns.net.src.services;

import android.location.Location;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import ddns.net.src.entities.LocationData;
import ddns.net.src.entities.UserData;
import ddns.net.src.properties.RabbitMQProperties;

public class LocationRabbitDataSource {

    private static final String QUEUE_EXCHANGE = RabbitMQProperties.RABBIT_MQ_QUEUE_EXCHANGE;
    private static final String QUEUE_ROUTING_KEY = RabbitMQProperties.RABBIT_MQ_QUEUE_ROUTING_KEY;

    private ConnectionFactory factory = new ConnectionFactory();
    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();

    public LocationRabbitDataSource() {
        setupConnectionFactory();
    }

    private void setupConnectionFactory() {
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUsername(RabbitMQProperties.RABBIT_MQ_SERVER_LOGIN);
            factory.setPassword(RabbitMQProperties.RABBIT_MQ_SERVER_PASSWORD);
            factory.setHost(RabbitMQProperties.RABBIT_MQ_SERVER_ADDRESS);
            factory.setPort(Integer.parseInt(RabbitMQProperties.RABBIT_MQ_SERVER_PORT));

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void sendLocation(LocationData locationData) {
        if(UserData.getId() != 0) {
            try {
                queue.addFirst(getJSONObject(locationData));

                Connection connection = factory.newConnection();
                Channel ch = connection.createChannel();
                ch.confirmSelect();

                String message = queue.takeFirst();

                try{

                    ch.basicPublish(
                            QUEUE_EXCHANGE,
                            QUEUE_ROUTING_KEY,
                            null,
                            message.getBytes());

                    ch.waitForConfirmsOrDie();

                } catch (Exception e) {
                    queue.putFirst(message);
                }finally {
                    ch.close();
                }
            } catch (InterruptedException | TimeoutException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getJSONObject(LocationData locationData){
        try {
            JSONObject locationObject = new JSONObject();
            locationObject.put("id",UserData.getId());
            locationObject.put("latitude",locationData.getLatitude());
            locationObject.put("longitude",locationData.getLongitude());

            return locationObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
