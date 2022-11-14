import datetime
import telebot
from telebot import types
import json
import requests

access_key = "5554978929:AAFy6DZZPV61ZpUdX-p5dQ7sQ77V10913RE"
bot = telebot.TeleBot(access_key)

def rating():
    response = requests.get("http://localhost:8080/api/getAll")
    students = json.loads(response.text)
    msg = ""
    i = 1
    for student in students:
        temp = ("{}) |{}| - {} {}.{}. ({})\n")
        msg += temp.format(i, student['points'], student['surname'], student['name'][0], student['patronymic'][0], student['groupName'])
        i+=1
    return msg

def my_rating(login):
    url = "http://localhost:8080/api/getByLogin/" + login
    response = requests.get(url)
    if response.ok:
        student = json.loads(response.text)
        temp = ("|{}| - {} {}.{}. ({})\n")
        msg = temp.format(student['points'], student['surname'], student['name'][0], student['patronymic'][0],
                           student['groupName'])
    else:
        msg = "Ошибка. Введите существующий логин."
    return msg

@bot.message_handler(commands=["start"])
def start(m, res=False):
    markup = types.ReplyKeyboardMarkup(resize_keyboard=True)
    item1 = types.KeyboardButton("Rating")
    markup.add(item1)
    bot.send_message(m.chat.id,
                     "Нажмите Rating для просмотра рейтинга.\nОтправьте свой логин для просмотра своих баллов.",
                     reply_markup=markup)


@bot.message_handler(content_types=["text"])
def handle_text(message):
    if message.text.strip() == "Rating":
        answer = rating()
    elif message.text.strip() == "Log":
        if (message.from_user.username == "moroz_zov"):
            answer = "Log file"
            doc = open('log.txt', 'rb')
            bot.send_document(message.chat.id, doc)
        else:
            answer = "Access is denied"
    else:
        answer = my_rating(message.text.strip())

    f = open('log.txt', 'a')
    msg = str(message.text.strip())
    user_name = str(message.from_user.username)
    if (user_name == "None"):
        user_name = "user(" + str(message.chat.id) + ")"

    log = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"), ' -- ', user_name, ': \"', msg, '\"\n'
    print(log)
    f.writelines(log)
    bot.send_message(message.chat.id, answer)

print("start")
bot.polling(none_stop=True, interval=0)
