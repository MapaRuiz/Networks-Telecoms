import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.awt.SystemColor.text;

public class TelegramBot extends TelegramLongPollingBot {

	@Override
    public String getBotUsername() {
        return "AlarmaMetanoBot";
    }

    @Override
    public String getBotToken() {
        return "7368091474:AAGlgF7QGDLcp6N_ueWT37XYSo2D8tayPnM";
    }

    @Override
    public void onUpdateReceived(Update update) {
    	//No se necesita implementar
    }

    SendMessage generateSendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(text);
        return sendMessage;
    }

    void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}