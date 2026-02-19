Clone repository
  git clone https://github.com/jelaev/pedestal-start.git [app-name]

Run repl (cider board on): 
  clj -M:nrepl
Then connect emacs/cider and load main buffer
If you want onl run to see
  clj -M:prod -m app.main
  http://localhost:8030/health
