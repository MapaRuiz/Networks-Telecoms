
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Redes {

    private static final String broker = "tcp://broker.emqx.io:1883";
    private static final String clientId = "JAVA_CLIENT";
    private static final String topic = "CANAL";
    private static final int subQos = 1;
    private static final int pubQos = 1;
    private static final String msg = "Hello MQTT I'm JAVA";
    
    // Umbral para activar la alerta
    private static final int umbral = 100;
    
    static Long chatId = (long) 6405088109L;
    static TelegramBot telegramBot = new TelegramBot();

    public static void run(){
        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            client.connect(options);
            client.subscribe(topic, subQos);

            if (client.isConnected()) {
            	
            	MqttMessage message0 = new MqttMessage(msg.getBytes());
                message0.setQos(pubQos);
                client.publish(topic, message0);
                
                client.setCallback(new MqttCallback() {
                	public void messageArrived(String topic, MqttMessage message) throws Exception {
                	    System.out.println("topic: " + topic);
                	    System.out.println("qos: " + message.getQos());
                	    System.out.println("message content: " + new String(message.getPayload()));

                	    try {
                	        // Convertir el mensaje a float
                	        float valorFloat = Float.parseFloat(new String(message.getPayload()));
                	        System.out.println("Valor como float: " + valorFloat);

                	        if (valorFloat > umbral) {
                	            String msg1 = "Alerta";
                	            MqttMessage message1 = new MqttMessage(msg1.getBytes());
                                message1.setQos(pubQos);
                                client.publish(topic, message1);
                                
                                telegramBot.sendMessage(telegramBot.generateSendMessage(chatId, "*Alerta* Se ha detectado gas metano"));
                	        }
                	        
                	    } catch (NumberFormatException e) {
                	        System.err.println("Error al convertir el mensaje a float: " + e.getMessage());
                	        // Maneja el error según tus necesidades
                	    }
                	    
                	    String MSG = new String(message.getPayload());
                	    System.out.println("MSG: " + MSG);
                	    
                	    if((MSG.equalsIgnoreCase("SERVOACTIVADO"))==true) {
                	    	Thread.sleep(5000);
                	    	String msg2 = "Solucionado";
                	    	MqttMessage message2 = new MqttMessage(msg2.getBytes());
                            message2.setQos(pubQos);
                            client.publish(topic, message2);
                            
                            telegramBot.sendMessage(telegramBot.generateSendMessage(chatId, "El servomotor ha sido activado correctamente"));
                	    }
                	}


                    public void connectionLost(Throwable cause) {
                        System.out.println("connectionLost: " + cause.getMessage());
                    }

                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("deliveryComplete: " + token.isComplete());
                    }
                });
                
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}