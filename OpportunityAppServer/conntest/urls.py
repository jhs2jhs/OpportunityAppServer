'''
Created on Nov 16, 2011

@author: jianhuashao
'''

from django.conf.urls.defaults import patterns, url

urlpatterns = patterns('OpportunityAppServer.conntest.views',
    url('^hello', 'display_hello'),
    url('^echo', 'echo'),
)