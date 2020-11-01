



func main() {
	martiniClassic := martini.Classic()
	guestBook := guestbook.NewGuestBook()
	webservice.RegisterWebService(guestBook, martiniClassic)
	martiniClassic.Run()
}