import copy

def path(node):
    if node is None or (lc(node) is None and rc(node) is None):
        return 0
    else:
        tmp1 = one_knot(node, True)
        tmp2 = one_knot(node, False)
        tmp3 = path(lc(node))
        tmp4 = path(rc(node))
        if lc(node) is None or rc(node) is None:
            return max(tmp1, tmp2, tmp3, tmp4)
        else:
            if val(lc(node)) < val(node) and val(node) < val(rc(node)):
                return max(one_knot(lc(node), True) + one_knot(rc(node), False) + 2, tmp1, tmp2, tmp3, tmp4)
            elif val(lc(node)) > val(node) and val(node) > val(rc(val)):
                return max(one_knot(lc(node), False) + one_knot(rc(node), True) + 2, tmp1, tmp2, tmp3, tmp4)
            else:
                return max(tmp1, tmp2, tmp3, tmp4)

def one_knot(node, flag):
    if lc(node) is None and rc(node) is None:
        return 0
    elif lc(node) is None:
        return check_node(node, rc(node), flag) + one_knot(rc(node))
    elif rc(node) is None:
        return check_node(node, lc(node), flag) + one_knot(lc(node))
    else:
        return max(check_node(node, rc(node), flag) + one_knot(rc(node)), check_node(node, lc(node), flag) + one_knot(lc(node)))

def check_node(node, other, flag):
    if flag is True:
        if val(node) > val(other)):
            return 1
        return 0
    else:
        if val(node) < val(other):
            return 1
        return 0
