import logging
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, scoped_session

logger = logging.getLogger(__name__)

class DB:
    Model = declarative_base()
    def __init__(self, uri=None, echo=False):
        self.echo = echo
        uri and self.init_db(uri, echo)

    def init_db(self, uri, echo=False):
        self.engine = create_engine(uri, pool_size=100, pool_recycle=5, pool_timeout=30,
                pool_pre_ping=True,
                echo=echo,
                max_overflow=0)
        self.uri = uri
        session = sessionmaker()
        session.configure(bind=self.engine)
        self.db_session = session()

    @property
    def Session(self):
        return self.db_session

    def drop_all(self):
        self.Model.metadata.drop_all(self.engine)

    def create_all(self):
        self.Model.metadata.create_all(self.engine)
