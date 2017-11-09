WinMinimizeAll()
Sleep(3000)
Local $1 = 100
While $1 >= 0
	Send("{VOLUME_DOWN}", 0)
	$1 = $1 - 1
WEnd

Sleep(1000)
Opt("WinTitleMatchMode",2)
WinActivate("Firefox","")
