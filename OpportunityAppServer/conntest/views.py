# Create your views here.

from django.http import HttpResponse, HttpRequest, HttpResponseRedirect
from django.shortcuts import render_to_response
import json

def display_hello(request):
    return HttpResponse("Hello")

def echo(request):
    key_values = []
    keys = request.REQUEST.keys()
    for key in keys:
        value = request.REQUEST.get(key)
        key_value = {key:value}
        key_values.append(key_value)
    print key_values
    out = {
        "server_conn":"successful",
        "params_from_clients":key_values
           }
    out_json = json.dumps(out, sort_keys=True)
    return HttpResponse(out_json)