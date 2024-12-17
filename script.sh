#!/bin/bash

# Запускаем первую команду в новом окне
osascript -e 'tell application "Terminal" to do script "ssh -p 2222 s381032@se.ifmo.ru -Y -L24082:helios:24082"'

# Пауза для уверенности, что первая команда стартовала
sleep 2

# Запускаем вторую команду в другом новом окне
osascript -e 'tell application "Terminal" to do script "ssh -p 2222 s381032@se.ifmo.ru -Y -L24080:helios:24080"'
