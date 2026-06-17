import os
import contextlib
with open(os.devnull, 'w') as f, contextlib.redirect_stdout(f):
    import pygame
    from pygame.locals import *
from OpenGL.GL import *
from OpenGL.GLU import *
from terraimmerse.util import get_resource_path


width = 1500
height = 800

def get_resolution():
    return width, height

def init_gl():
    pygame.init()
    pygame.display.gl_set_attribute(pygame.GL_CONTEXT_MAJOR_VERSION, 3)
    pygame.display.gl_set_attribute(pygame.GL_CONTEXT_MINOR_VERSION, 3)
    pygame.display.gl_set_attribute(
        pygame.GL_CONTEXT_PROFILE_MASK,
        pygame.GL_CONTEXT_PROFILE_CORE
    )
    pygame.display.set_mode((width, height), DOUBLEBUF | OPENGL, vsync=1)
    pygame.display.set_caption("TerraImmerse")
    pygame.display.set_icon(
        pygame.image.load(get_resource_path("assets/icon.png"))
    )
    glEnable(GL_DEPTH_TEST)
    glEnable(GL_CULL_FACE)
    glCullFace(GL_BACK)
    glFrontFace(GL_CCW)
    glViewport(0, 0, width, height)