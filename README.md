<p>Clone repository
  <code>git clone https://github.com/jelaev/pedestal-start.git [app-name]</code></p>
<p><code>cd [app-name]</code></p>
<p><code>rm -rf .git</code></p>
<p><code>git init</code></p>

<p>Run repl (cider board on): 
  <code>clj -M:nrepl</code></p>
<p>Then connect emacs/cider and load main buffer</p>
<p>If you want onl run to see
  <code>clj -M:prod -m app.main</code>
  go to
  <code>http://localhost:8030/health</code></p>
