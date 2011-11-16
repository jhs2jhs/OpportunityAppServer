Server running envrionemnt:
-- python 2.7 : http://python.org/ 
-- django 1.3.1 : https://www.djangoproject.com/
-- sqlite3 : http://www.sqlite.org/
-- eclipse 3.7.1 with pydev plugin

Starting server: see screenshots 1 & 2
-- start server in terminal with this cmd: python manage.py run server 8000 
-- testing server running with web browser: http://localhost:8000/conntest/echo?hello=world&foo=bar 
-- you would be able to see a json showing on browser: {"params_from_clients": [{"foo": "bar"}, {"hello": "world"}], "server_conn": "successful"}

What does it do?
-- it would get any HTTP request (POST or GET) and then wrap it with connection state for a new json. If no error, it would then return a json string back to client, now matter whether it is a web browser or mobile app request. 
-- you can run this server on your local machine, connect your mobile to your pc in ad hoc sharing mode, check your mobile sdk what is relevant to your localhost accessing address in your mobile
